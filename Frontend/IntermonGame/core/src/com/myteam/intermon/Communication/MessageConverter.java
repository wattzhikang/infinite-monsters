package com.myteam.intermon.Communication;

import com.badlogic.gdx.utils.JsonValue;

public class MessageConverter
{
    public String setMessage(JsonValue message)
    {
        String m = "{" + message.toString();
        for(JsonValue current = message.child; current != null; current = current.next)
        {
            m += ", " + current.toString();
        }
        m += "}";
        return m;
    }
}
