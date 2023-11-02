
(ns pretty-elements.select.env
    (:require [pretty-elements.plain-field.env :as plain-field.env]
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
  ; XXX#0569 (source-code/cljs/pretty_elements/combo_box/env.cljs)
  (let [field-content (plain-field.env/get-field-content :pretty-elements.select/option-field)
        option-label  (-> option option-label-f metamorphic-content/compose)]
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
  (if-let [selected-option-label @(r/subscribe [:pretty-elements.select/get-selected-option-label select-id select-props])]
          (-> selected-option-label metamorphic-content/compose)
          (-> :select!              metamorphic-content/compose)))
