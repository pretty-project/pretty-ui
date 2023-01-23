
(ns elements.multi-field.helpers
    (:require [hiccup.api       :as hiccup]
              [noop.api         :refer [return]]
              [re-frame.api     :as r]
              [vector.api       :as vector]
              [x.components.api :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props->single-field?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props _]
  (let [group-value @(r/subscribe [:elements.multi-field/get-group-value group-id group-props])]
       (vector/count? group-value 1)))

(defn group-props->multi-field?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props field-dex]
  (let [single-field? (group-props->single-field? group-id group-props field-dex)]
       (not single-field?)))

(defn group-props->min-field-count-reached?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props _]
  (let [group-value @(r/subscribe [:elements.multi-field/get-group-value group-id group-props])]
       (vector/count? group-value 1)))

(defn group-props->max-field-count-reached?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:max-input-count (integer)}
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id {:keys [max-input-count] :as group-props} _]
  (let [group-value @(r/subscribe [:elements.multi-field/get-group-value group-id group-props])]
       (vector/count? group-value max-input-count)))

(defn field-dex->field-label
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:label (metamorphic-content)}
  ; @param (integer) field-dex
  ;
  ; @return (metamorphic-content)
  [group-id {:keys [label] :as group-props} field-dex]
        ; Single-field label
  (cond (and    label (group-props->single-field? group-id group-props field-dex))
        (return label)
        ; Multi-field label
        (and label (group-props->multi-field? group-id group-props field-dex))
        (x.components/content {:content label :suffix (str " #" (inc field-dex))})))

(defn field-dex->control-adornments
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (maps in vector)
  [group-id group-props field-dex]
  [{:icon            :add
    :disabled?       (group-props->max-field-count-reached?       group-id group-props field-dex)
    :on-click        [:elements.multi-field/increase-field-count! group-id group-props field-dex]
    :tooltip-content :add-field!}
   {:disabled?       (group-props->min-field-count-reached?       group-id group-props field-dex)
    :icon            :close
    :on-click        [:elements.multi-field/decrease-field-count! group-id group-props field-dex]
    :tooltip-content :remove-field!}])

(defn field-dex->end-adornments
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:end-adornments (maps in vector)(opt)}
  ; @param (integer) field-dex
  ;
  ; @return (maps in vector)
  [group-id {:keys [end-adornments] :as group-props} field-dex]
  (vector/concat-items end-adornments (field-dex->control-adornments group-id group-props field-dex)))

(defn field-dex->autofocus?
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:autofocus? (boolean)(opt)}
  ; @param (integer) field-dex
  ;
  ; @return (vector)
  [_ {:keys [autofocus?]} field-dex]
  ; Az első mezőre a group-props térképben átadott autofocus? tulajdonság érvényes,
  ; minden további mező a hozzáadódása utáni mellékhatás esemény által kapja meg a fókuszt!
  ;
  ; BUG#9111
  ; Az x4.7.7 verzióig a további mezők {:autofocus? true} beállítással jelentek meg,
  ; ezért ha egy multi-field elem a React-fába csatolódásakor már több értékkel rendelkezett,
  ; akkor az első mezőt leszámítva az összes többi mező {:autofocus? true} beállítással jelent meg!
  (if (= field-dex 0)
      (return autofocus?)))

(defn field-dex->value-path
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (vector)}
  ; @param (integer) field-dex
  ;
  ; @return (vector)
  [_ {:keys [value-path]} field-dex]
  (vector/conj-item value-path field-dex))

(defn field-dex->react-key
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @example
  ; (field-dex->react-key :my-group {...} 3)
  ; =>
  ; "my-group--3"
  ;
  ; @return (string)
  [group-id _ field-dex]
  (hiccup/value group-id field-dex))

(defn field-dex->field-id
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @example
  ; (field-dex->field-id :my-group {...} 3)
  ; =>
  ; :my-group--3
  ;
  ; @return (string)
  [group-id _ field-dex]
  (keyword      (namespace group-id)
           (str (name      group-id) "--" field-dex)))
