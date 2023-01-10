
(ns elements.multi-combo-box.prototypes
    (:require [candy.api                        :refer [param return]]
              [elements.input.helpers           :as input.helpers]
              [elements.multi-combo-box.helpers :as multi-combo-box.helpers]
              [loop.api                         :refer [<-walk]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:value-path (vector)}
  [box-id box-props]
  ; XXX#5061
  ; XXX#5062
  (let [field-id    (multi-combo-box.helpers/box-id->field-id box-id)
        field-props (dissoc box-props :class :helper :indent :label :outdent :style :value-path)]
       (merge {:value-path (input.helpers/default-value-path field-id)}
              (param field-props))))

(defn group-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:chip-label-f (function)
  ;  :deletable? (boolean)
  ;  :indent (map)}
  [box-id box-props]
  (let [group-props (dissoc box-props :class :helper :label :indent :outdent :placeholder :style)]
       (merge {:chip-label-f return
               :indent {:bottom :xxs}}
              (param group-props)
              {:deletable? true})))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:field-value-f (function)
  ;  :on-blur (metamorphic-event)
  ;  :on-change (metamorphic-event)
  ;  :on-focus (metamorphic-event)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-path (vector)}
  [box-id box-props]
  ; XXX#5067 (source-code/cljs/elements/combo_box/prototypes.cljs)
  ; XXX#5061
  ; The value-path of the multi-combo-box element is where the selected values
  ; stored in the application state.
  ; The combo-box element implemented in the multi-combo-box has its own value-path
  ; which is where the field content stored in the application state.
  ; Therefore the value-path property isn't inherited from the multi-combo-box
  ; to the combo-box.
  ;
  ; XXX#5062
  ; The options-path of multi-combo-box element is the same as the options-path
  ; of the implemented combo-box element.
  ; Therefore the options-path property is inherited from the multi-combo-box
  ; to the combo-box.
  (<-walk {:field-value-f  return
           :option-label-f return
           :option-value-f return
           :options-path   (input.helpers/default-options-path box-id)
           :value-path     (input.helpers/default-value-path   box-id)}
         (fn [%] (merge % box-props))
         (fn [%] (merge % {:on-blur   [:elements.multi-combo-box/field-blurred box-id %]
                           :on-change [:elements.multi-combo-box/field-changed box-id %]
                           :on-focus  [:elements.multi-combo-box/field-focused box-id %]}))))
