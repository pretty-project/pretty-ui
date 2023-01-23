
(ns elements.select.helpers
    (:require [elements.plain-field.helpers :as plain-field.helpers]
              [re-frame.api                 :as r]
              [string.api                   :as string]
              [x.components.api             :as x.components]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-option?
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-label-f (function)(opt)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [select-id {:keys [option-label-f] :as select-props} option]
  ; XXX#0714 (source-code/cljs/elements/combo_box/helpers.cljs)
  (let [field-content (plain-field.helpers/get-field-content :elements.select/option-field)
        option-label  (-> option option-label-f x.components/content)]
       (and (string/not-pass-with? option-label field-content {:case-sensitive? false})
            (string/starts-with?   option-label field-content {:case-sensitive? false}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-label
  ; @ignore
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (metamorphic-content)
  [select-id select-props]
  (if-let [selected-option-label @(r/subscribe [:elements.select/get-selected-option-label select-id select-props])]
          (-> selected-option-label x.components/content)
          (-> :select!              x.components/content)))
