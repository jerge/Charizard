package alexa.projectcharizard.Model;

/**
 * An enum for the different Categories of a spot
 */
public enum Category {
    FRUIT {
        public String toString() {
            return "Fruit";
        }
    },
    VEGETABLE {
        public String toString() {
            return "Vegetable";
        }
    },
    BERRY {
        public String toString() {
            return "Berry";
        }
    },
    MUSHROOM {
        public String toString() {
            return "Mushroom";
        }
    },
    OTHER {
        public String toString() {
            return "Other";
        }
    }
}


