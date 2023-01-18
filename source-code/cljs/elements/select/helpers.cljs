
(ns elements.select.helpers
    (:require [elements.plain-field.helpers :as plain-field.helpers]
              [re-frame.api                 :as r]
              [string.api                   :as string]
              [x.components.api             :as x.components]
              [x.environment.api            :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-option?
  ; WARNING! NON-PUBLIC! DO NOT USE!
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

(defn select-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:option-value-f (function)
  ;  :value-path (vector)}
  ; @param (*) option
  ;
  ; @return (map)
  ; {:data-click-effect (keyword)
  ;  :data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-letter-spacing (keyword)
  ;  :data-selected (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [select-id {:keys [option-value-f value-path] :as select-props} option]
  (let [selected-value  @(r/subscribe [:x.db/get-item value-path])
        option-value     (option-value-f option)
        option-selected? (= selected-value option-value)
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:data-click-effect   :opacity
        :data-font-size      :s
        :data-font-weight    (if option-selected? :bold :medium)
        :data-letter-spacing :auto
        :data-line-height    :text-block
        :data-selected       option-selected?
        :on-click            #(r/dispatch on-click)
        :on-mouse-up         #(x.environment/blur-element! select-id)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-label
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (metamorphic-content)
  [select-id select-props]
  (if-let [selected-option-label @(r/subscribe [:elements.select/get-selected-option-label select-id select-props])]
          (-> selected-option-label x.components/content)
          (-> :select!              x.components/content)))
