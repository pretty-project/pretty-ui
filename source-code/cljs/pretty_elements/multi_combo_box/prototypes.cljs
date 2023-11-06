
(ns pretty-elements.multi-combo-box.prototypes
    (:require [pretty-elements.input.utils           :as input.utils]
              [pretty-elements.multi-combo-box.utils :as multi-combo-box.utils]
              [loop.api                       :refer [<-walk]]
              [noop.api                       :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn field-props-prototype
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:value-path (Re-Frame path vector)}
  [box-id box-props]
  ; XXX#5061
  ; XXX#5062
  (let [field-id    (multi-combo-box.utils/box-id->field-id box-id)
        field-props (dissoc box-props :class :helper :indent :label :outdent :style :value-path)]
       (merge {:value-path (input.utils/default-value-path field-id)}
              (-> field-props))))

(defn group-props-prototype
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {}
  ;
  ; @return (map)
  ; {:chips-path (Re-Frame path vector)
  ;  :deletable? (boolean)}
  [_ {:keys [chip-group value-path]}]
  (merge {:chips-path value-path
          :deletable? true}
         (-> chip-group)))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:on-blur (Re-Frame metamorphic-event)(opt)
  ;  :on-changed (Re-Frame metamorphic-event)(opt)
  ;  :on-focus (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:field-value-f (function)
  ;  :on-blur (Re-Frame metamorphic-event)
  ;  :on-change (Re-Frame metamorphic-event)
  ;  :on-focus (Re-Frame metamorphic-event)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-path (Re-Frame path vector)}
  [box-id {:keys [on-blur on-changed on-focus] :as box-props}]
  ; XXX#5067 (source-code/cljs/pretty_elements/combo_box/prototypes.cljs)
  ; XXX#5068 (source-code/cljs/pretty_elements/text_field/prototypes.cljs)
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
           :options-path   (input.utils/default-options-path box-id)
           :value-path     (input.utils/default-value-path   box-id)}
          (fn [%] (merge % box-props))
          (fn [%] (merge % {:on-blur    {:dispatch-n [on-blur    [:pretty-elements.multi-combo-box/field-blurred box-id %]]}
                            :on-changed {:dispatch-n [on-changed [:pretty-elements.multi-combo-box/field-changed box-id %]]}
                            :on-focus   {:dispatch-n [on-focus   [:pretty-elements.multi-combo-box/field-focused box-id %]]}}))))