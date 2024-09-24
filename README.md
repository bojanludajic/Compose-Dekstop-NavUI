# Navigation Graph Code Generator

This project is a Compose Desktop application for generating navigation graphs visually.
Users can place nodes and edges to represent screens and their connections, which can then be converted into code.<br>
The main motivation for this application stems from the fact that Compose Desktop doesn't feature a native navigation implementation.

## Features

- **Interactive Graph Drawing**: Users can add nodes to the graph by clicking on the drawing area.
- **Node Placement Toggle**: Easily switch between placing nodes and viewing the graph.
- **Edge Management**: Create and delete edges between nodes.
- **Code Generation**: Generate code for the navigation graph and copy it to the clipboard.

## Usage

1. **Placing Nodes**: Click the "+" icon to enter node placement mode. Click on the green area to place nodes.
2. **Connecting Nodes**: Click the two nodes you want to connect one by one, if connected successfully, a line should appear between them.
3. **Generating Code**: Click the "Generate code" button to copy the graph representation to your clipboard.
4. **Moving Nodes**: Click and drag a node with your mouse to where you want it to be located.
5. **Clearing the Graph**: Use the delete icon to remove all nodes and edges.

## Installation

1. Clone the repository:
   `git clone https://github.com/bojanludajic/Compose-Dekstop-NavUI`
2. Open the project in your IDE.
3. Build and run the app on your desktop environment.

## Dependencies

- Compose Desktop
- KotlinPoet

## Future updates

- Implement third-party libraries to enable more advanced navigation graph generation.
- Graph design should be more informative.

## Screenshots

<table>
  <tr>
    <td>
      <img width="500" height="200" alt="Screenshot 1" src="https://github.com/user-attachments/assets/eecdcf07-82ff-4c36-9452-c9b46043e67b">
      <p>Empty graph.</p>
    </td>
    <td>
      <img width="500" height="200" alt="Screenshot 2" src="https://github.com/user-attachments/assets/1dc9b650-b6f5-4264-8f4d-a9e88414966e">
      <p>Screen placing toggled on.</p>
    </td>
    <td>
      <img width="500" height="200" alt="Screenshot 3" src="https://github.com/user-attachments/assets/78a7efa8-6b90-4207-833c-02d1d5e26ea1">
      <p>Basic graph with 5 screens.</p>
    </td>
  </tr>
</table>
