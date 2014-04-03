package com.example.fairlight.entities;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

import java.util.List;

/**
 * Created by ilewis on 2/18/14.
 */
@Entity
public class TaskList {

    @Parent Key<Task> parent;
    @Id Long id;

    List<Ref<Task>> tasks;
}
