package com.georgen.hawthorne.model.settings.yaml;

public class HawthorneNode {
    private NamingNode naming;
    public NamingNode getNaming() { return naming; }

    public void setNaming(NamingNode namingNode) { this.naming = namingNode; }
    public boolean isEmpty(){
        return this.naming == null || this.naming.isEmpty();
    }
}
