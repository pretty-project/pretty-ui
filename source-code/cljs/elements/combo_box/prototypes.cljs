
(ns elements.combo-box.prototypes
    (:require [elements.input.utils :as input.utils]
              [loop.api             :refer [<-walk]]
              [noop.api             :refer [return]]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn box-props-prototype
  ; @ignore
  ;
  ; @param (keyword) box-id
  ; @param (map) box-props
  ; {:on-blur (Re-Frame metamorphic-event)(opt)
  ;  :on-changed (Re-Frame metamorphic-event)(opt)}
  ;  :on-focus (Re-Frame metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:field-content-f (function)
  ;  :field-value-f (function)
  ;  :on-blur (Re-Frame metamorphic-event)
  ;  :on-changed (Re-Frame metamorphic-event)
  ;  :on-focus (Re-Frame metamorphic-event)
  ;  :option-label-f (function)
  ;  :option-value-f (function)
  ;  :options-path (Re-Frame path vector)}
  [box-id {:keys [on-blur on-changed on-focus] :as box-props}]
  ; XXX#5068 (source-code/cljs/elements/text_field/prototypes.cljs)
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
  (<-walk {:field-content-f return
           :field-value-f   return
           :option-label-f  return
           :option-value-f  return
           :options-path    (input.utils/default-options-path box-id)
           :value-path      (input.utils/default-value-path   box-id)}
          (fn [%] (merge % box-props))
          (fn [%] (merge % {:on-blur    {:dispatch-n [on-blur    [:elements.combo-box/field-blurred box-id %]]}
                            :on-changed {:dispatch-n [on-changed [:elements.combo-box/field-changed box-id %]]}
                            :on-focus   {:dispatch-n [on-focus   [:elements.combo-box/field-focused box-id %]]}}))))
