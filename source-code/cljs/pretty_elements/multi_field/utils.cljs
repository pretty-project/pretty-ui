
(ns pretty-elements.multi-field.utils
    (:require [hiccup.api :as hiccup]
              [vector.api :as vector]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

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
  (if (=  field-dex 0)
      (-> autofocus?)))

(defn field-dex->value-path
  ; @ignore
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; {:value-path (Re-Frame path vector)}
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
