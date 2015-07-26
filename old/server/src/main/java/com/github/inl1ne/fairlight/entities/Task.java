package com.github.inl1ne.fairlight.entities;

import com.github.inl1ne.arc.InjectCrud;
import com.github.inl1ne.fairlight.FairlightApi;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

import java.util.List;

/**
 * Created by ilewis on 2/13/14.
 */
@Entity
@InjectCrud(ApiClass = FairlightApi.class, EntityPath = "task")
public class Task {
    @Id Long id;
    String name;


    Ref<User> owner;
    List<Ref<User>> collaborators;

    Ref<TaskList> predecessors;
    Ref<TaskList> subtasks;
}
