package ru.solodkov.voipadmin.autoprovisioning.service.builder.util;


import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.Objects.isNull;

public class PlainTextConfigAttrBuilder {

    private static final String DEFAULT_ASSIGNMENT_SYMBOL = " = ";

    private final List<String> sourceLines;
    private final String assignmentSymbol;

    public PlainTextConfigAttrBuilder(String source) {
        this.sourceLines = source.lines().collect(Collectors.toList());
        this.assignmentSymbol = DEFAULT_ASSIGNMENT_SYMBOL;
    }

    public PlainTextConfigAttrBuilder(String sourceString, String assignmentSymbol) {
        this.sourceLines = sourceString.lines().collect(Collectors.toList());
        this.assignmentSymbol = assignmentSymbol;
    }

    public PlainTextConfigAttrBuilder set(String attrName, Object attrValue) {
        if (isNull(attrValue)) {
            return this;
        }
        OptionalInt matchIndex = IntStream.range(0, sourceLines.size())
            .parallel()
            .filter(index -> sourceLines.get(index).contains(attrName))
            .findFirst();
        matchIndex.ifPresent(index -> sourceLines.set(index, attrName + assignmentSymbol + attrValue));
        return this;
    }

    public PlainTextConfigAttrBuilder add(String attrName, Object attrValue) {
        sourceLines.add(attrName + assignmentSymbol + attrValue);
        return this;
    }

    public PlainTextConfigAttrBuilder addNewLine() {
        sourceLines.add("");
        return this;
    }

    public PlainTextConfigAttrBuilder addComment(String comment) {
        sourceLines.add(comment);
        return this;
    }

    public PlainTextConfigAttrBuilder remove(String attrName) {
        List<String> linesContainingAttrName = sourceLines.parallelStream()
            .filter(line -> line.contains(attrName))
            .collect(Collectors.toList());
        if (!linesContainingAttrName.isEmpty()) {
            sourceLines.removeAll(linesContainingAttrName);
        }
        return this;
    }

    @Override
    public String toString() {
        return sourceLines.stream().collect(Collectors.joining(System.lineSeparator()));
    }
}
