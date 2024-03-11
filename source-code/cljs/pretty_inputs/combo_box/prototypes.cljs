
(ns pretty-inputs.combo-box.prototypes
    (:require [fruits.string.api        :as string]
              [pretty-inputs.engine.api :as pretty-inputs.engine]
              [pretty-properties.api    :as pretty-properties]
              [pretty-models.api :as pretty-models]
              [pretty-subitems.api      :as pretty-subitems]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn option-group-prototype
  ; @ignore
  ;
  ; @param (keyword) id
  ; @param (map) props
  ; {:field (map)(opt)
  ;  ...}
  ; @param (map) option-group
  ;
  ; @return (map)
  [id {:keys [field]} option-group]
  (let [field-id         (pretty-subitems/subitem-id id :field)
        field-content    (pretty-inputs.engine/get-input-field-displayed-content field-id field)
        option-compare-f (fn [%] (string/starts-with? % field-content {:case-sensitive? false}))
        option-filter-f  (fn [%] (-> % :label :content option-compare-f))]
       (-> option-group (pretty-properties/default-input-option-props {:option-filter-f option-filter-f})
                        (pretty-properties/default-outer-size-props   {:outer-width :parent}))))

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
  (let [option-group-prototype-f (fn [%] (option-group-prototype id props %))]
       (-> props (pretty-properties/default-flex-props       {:gap :xs :horizontal-align :left :orientation :vertical})
                 (pretty-properties/default-outer-size-props {:outer-size-unit :full-block})
                 (pretty-models/flex-container-standard-props)
                 (pretty-models/flex-container-rules)
                 (pretty-subitems/ensure-subitem           :field)
                 (pretty-subitems/subitems<-disabled-state :header :field :option-group)
                 (pretty-subitems/apply-subitem-prototype  :option-group option-group-prototype-f))))






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
