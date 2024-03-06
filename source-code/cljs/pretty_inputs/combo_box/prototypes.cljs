
(ns pretty-inputs.combo-box.prototypes
    (:require [fruits.loop.api       :refer [<-walk]]
              [fruits.noop.api       :refer [return]]
              [pretty-properties.api :as pretty-properties]
              [pretty-rules.api      :as pretty-rules]
              [pretty-standards.api  :as pretty-standards]
              [pretty-subitems.api  :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn props-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ;
  ; @return (map)
  [id props]
  (-> props (pretty-properties/default-flex-props       {:gap :xs :horizontal-align :left :orientation :vertical})
            (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
            (pretty-standards/standard-flex-props)
            (pretty-standards/standard-inner-position-props)
            (pretty-standards/standard-inner-size-props)
            (pretty-standards/standard-outer-position-props)
            (pretty-standards/standard-outer-size-props)
           ;(pretty-rules/auto-align-scrollable-flex)
            (pretty-rules/auto-set-mounted)
            (pretty-subitems/ensure-subitem           :field)
            (pretty-subitems/subitems<-disabled-state :header :field :option-group)
            (pretty-subitems/leave-disabled-state     :header :field :option-group)))







  ;(<-walk box-props))
          ;(fn [%] (pretty-defaults/use-default-values % {:field-content-f return :field-value-f return :option-label-f return :option-value-f return})))
          ;(fn [%] (pretty-defaults/force-values  % {:on-blur      {:dispatch-n [on-blur    [:pretty-inputs.combo-box/field-blurred box-id %]]}
          ;                                          :on-changed   {:dispatch-n [on-changed [:pretty-inputs.combo-box/field-changed box-id %]]}
          ;                                          :on-focus     {:dispatch-n [on-focus   [:pretty-inputs.combo-box/field-focused box-id %]]})))




  ; XXX#5068 (source-code/cljs/pretty_inputs/text_field/prototypes.cljs)
  ; XXX#5067
  ; The 'field-content-f' and 'field-value-f' properties are belong to the
  ; 'text-field' element, but they have to be in the prototype of the 'combo-box'
  ; element also, because some of the keypress events of the 'combo-box' element
  ; uses these properties.
  ; E.g., The '[:pretty-inputs.combo-box/field-focused ...]' event registers keypress
  ;       events for the 'combo-box' which events can dispatch the original keypress
  ;       events of the 'text-field'.
  ;       For example the ESC button event of the 'combo-box' dispatches the ESC button
  ;       event of the 'text-field' which dispatches the '[:pretty-inputs.text-field/empty-field! ...]'
  ;       event which requires the 'field-value-f' function.
  ;(<-walk {:field-content-f return
  ;         :field-value-f   return
  ;         :option-label-f  return
  ;         :option-value-f  return
  ;        (fn [%] (merge % box-props))
  ;        (fn [%] (merge % {:on-blur    {:dispatch-n [on-blur    [:pretty-inputs.combo-box/field-blurred box-id %]]}
  ;                          :on-changed {:dispatch-n [on-changed [:pretty-inputs.combo-box/field-changed box-id %]]}
  ;                          :on-focus   {:dispatch-n [on-focus   [:pretty-inputs.combo-box/field-focused box-id %]]}))
