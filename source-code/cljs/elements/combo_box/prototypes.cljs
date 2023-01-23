
(ns elements.combo-box.prototypes
    (:require [elements.input.helpers :as input.helpers]
              [loop.api               :refer [<-walk]]
              [noop.api               :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ;
  ; @return (map)
  ; {:field-content-f (function)
  ;  :field-value-f (function)
  ;  :on-blur (metamorphic-event)
  ;  :on-change (metamorphic-event)
  ;  :on-focus (metamorphic-event)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-path (vector)}
  [box-id box-props]
  ; XXX#5067
  ; The 'field-content-f' and 'field-value-f' properties are belongs to the
  ; text-field element, but they has to be set in the prototype of the combo-box
  ; element too, because some of the keypress events of the combo-box element
  ; uses these properties.
  ; E.g. The [:elements.combo-box/field-focused ...] event registers keypress
  ;      events for the combo-box which events can dispatch the original keypress
  ;      events of the text-field.
  ;      For example the ESC button event of the combo-box dispatches the ESC button
  ;      event of the text-field which dispatches the [:elements.text-field/empty-field! ...]
  ;      event which requires the 'field-value-f' function.
  ;
  ; XXX#5068
  ; By using the '<-walk' function the :on-blur, :on-change and :on-focus
  ; events takes the 'box-props' map AFTER it being merged with the default values!
  (<-walk {:field-content-f return
           :field-value-f   return
           :option-label-f  return
           :option-value-f  return
           :options-path    (input.helpers/default-options-path box-id)
           :value-path      (input.helpers/default-value-path   box-id)}
          (fn [%] (merge % box-props))
          (fn [%] (merge % {:on-blur   [:elements.combo-box/field-blurred box-id %]
                            :on-change [:elements.combo-box/field-changed box-id %]
                            :on-focus  [:elements.combo-box/field-focused box-id %]}))))
