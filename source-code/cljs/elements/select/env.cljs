
(ns elements.select.env
    (:require [elements.plain-field.env :as plain-field.env]
              [metamorphic-content.api  :as metamorphic-content]
              [re-frame.api             :as r]
              [string.api               :as string]))

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
  ; XXX#0569 (source-code/cljs/elements/combo_box/env.cljs)
  (let [field-content (plain-field.env/get-field-content :elements.select/option-field)
        option-label  (-> option option-label-f metamorphic-content/resolve)]
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
          (-> selected-option-label metamorphic-content/resolve)
          (-> :select!              metamorphic-content/resolve)))
