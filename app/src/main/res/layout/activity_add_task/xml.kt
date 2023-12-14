package layout.activity_add_task
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
xmlns:app="http://schemas.android.com/apk/res-auto"
xmlns:tools="http://schemas.android.com/tools"
android:layout_width="match_parent"
android:layout_height="match_parent"
android:orientation="vertical"
android:padding="16dp"
tools:context=".AddTaskActivity">

<EditText
android:id="@+id/editTextTitle"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="Заголовок задачи" />

<EditText
android:id="@+id/editTextDescription"
android:layout_width="match_parent"
android:layout_height="wrap_content"
android:hint="Описание задачи" />

<Button
android:id="@+id/buttonAddTask"
android:layout_width="wrap_content"
android:layout_height="wrap_content"
android:text="Добавить задачу" />

</LinearLayout>