
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.multi-field.helpers
    (:require [mid-fruits.candy               :refer [return]]
              [mid-fruits.hiccup              :as hiccup]
              [mid-fruits.vector              :as vector]
              [re-frame.api                   :as r]
              [x.app-components.api           :as components]
              [x.app-elements.element.helpers :as element.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-props->single-field?
  ; WARNING! NON-PUBLIC! DO NOT USE!
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
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props field-dex]
  (let [single-field? (group-props->single-field? group-id group-props field-dex)]
       (not single-field?)))

(defn group-props->max-field-count-reached?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:max-input-count (integer)}
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id {:keys [max-input-count] :as group-props} _]
  (let [group-value @(r/subscribe [:elements.multi-field/get-group-value group-id group-props])]
       (vector/count? group-value max-input-count)))

(defn field-dex->last-field?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (boolean)
  [group-id group-props field-dex]
  (let [group-value @(r/subscribe [:elements.multi-field/get-group-value group-id group-props])]
       (vector/dex-last? group-value field-dex)))

(defn field-dex->field-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:label (metamorphic-content)}
  ; @param (integer) field-dex
  ;
  ; @return (metamorphic-content)
  [group-id {:keys [label] :as group-props} field-dex]
        ; Single-field label
  (cond (and    label (group-props->single-field? group-id group-props field-dex))
        (return label)
        ; Multi-field label
        (and label (group-props->multi-field? group-id group-props field-dex))
        (components/content {:content label :suffix (str " #" (inc field-dex))})))

(defn field-dex->end-adornments
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @return (maps in vector)
  [group-id group-props field-dex]
        ; Multiple field & last field & maximum field count reached
  (cond (and (field-dex->last-field?                group-id group-props field-dex)
             (group-props->multi-field?             group-id group-props field-dex)
             (group-props->max-field-count-reached? group-id group-props field-dex))
        [{:icon     :close
          :on-click [:elements.multi-field/decrease-field-count! group-id group-props field-dex]
          :tooltip  :delete-field!}]
        ; Multiple field & last field
        (and (field-dex->last-field?    group-id group-props field-dex)
             (group-props->multi-field? group-id group-props field-dex))
        [{:icon     :add
          :on-click [:elements.multi-field/increase-field-count! group-id group-props {:initial-value ""}]
          :tooltip  :add-field!}
         {:icon     :close
          :on-click [:elements.multi-field/decrease-field-count! group-id group-props field-dex]
          :tooltip  :delete-field!}]
        ; Single field
        (group-props->single-field? group-id group-props field-dex)
        [{:icon     :add
          :on-click [:elements.multi-field/increase-field-count! group-id group-props {:initial-value ""}]
          :tooltip  :add-field!}]
        ; Single field & not the last field
        (group-props->multi-field? group-id group-props field-dex)
        [{:icon     :close
          :on-click [:elements.multi-field/decrease-field-count! group-id group-props field-dex]
          :tooltip  :delete-field!}]))

(defn field-dex->autofocus?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:autofocus? (boolean)(opt)}
  ; @param (integer) field-dex
  ;
  ; @return (vector)
  [_ {:keys [autofocus?]} field-dex]
  ; Az első mezőre a group-props térképben átadott autofocus? tulajdonság érvényes,
  ; minden további mező {:autofocus? true} beállítással jelenik meg.
  (if (=      field-dex 0)
      (return autofocus?)
      (return true)))

(defn field-dex->value-path
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;  {:value-path (vector)}
  ; @param (integer) field-dex
  ;
  ; @return (vector)
  [_ {:keys [value-path]} field-dex]
  (vector/conj-item value-path field-dex))

(defn field-dex->react-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ; @param (integer) field-dex
  ;
  ; @example
  ;  (field-dex->react-key :my-group {} 3)
  ;  =>
  ;  "my-group--3"
  ;
  ; @return (string)
  [group-id _ field-dex]
  (hiccup/value group-id field-dex))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn group-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) group-id
  ; @param (map) group-props
  ;
  ; @return (map)
  ;  {}
  [group-id group-props]
  ; Az egyes mezők is reagálnak a {:disabled? true} állapotra, ezért a mezőcsoportnak,
  ; már nem szükséges reagálni rá, különben a kétszeresen jelenne meg a sötétítő
  ; hatás (a mezőkön is és a mezőcsoporton is)!
  (let [group-props (dissoc group-props :disabled?)]
       (merge (element.helpers/element-default-attributes group-id group-props)
              (element.helpers/element-indent-attributes  group-id group-props)
              {})))
