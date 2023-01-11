
(ns elements.select.helpers
    (:require [candy.api                    :refer [return]]
              [elements.element.helpers     :as element.helpers]
              [elements.plain-field.helpers :as plain-field.helpers]
              [elements.select.config       :as select.config]
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
  ;  :data-selected (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [select-id {:keys [option-value-f value-path] :as select-props} option]
  (let [selected-value  @(r/subscribe [:x.db/get-item value-path])
        option-value     (option-value-f option)
        option-selected? (= selected-value option-value)
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:data-click-effect :opacity
        :data-font-size    :s
        :data-font-weight  (if option-selected? :extra-bold :bold)
        :data-selected     option-selected?
        :on-click          #(r/dispatch on-click)
        :on-mouse-up       #(x.environment/blur-element! select-id)}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:border-color (keyword)
  ;  :border-radius (keyword)
  ;  :disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ; {:data-border-color (keyword)
  ;  :data-border-radius (keyword)
  ;  :data-click-effect (keyword)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [select-id {:keys [border-color border-radius disabled?] :as select-props}]
  (let [on-click [:elements.select/render-options! select-id select-props]]
       (merge {:data-border-color  border-color
               :data-border-radius border-radius}
              (if disabled? {:disabled          true}
                            {:data-click-effect :opacity
                             :on-click    #(r/dispatch on-click)
                             :on-mouse-up #(x.environment/blur-element! select-id)}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:layout (keyword)
  ;  :min-width (keyword)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:data-element-width (keyword)
  ;  :data-layout (keyword)
  ;  :style (map)}
  [select-id {:keys [border-radius layout min-width style] :as select-props}]
  (merge (element.helpers/element-indent-attributes select-id select-props)
         {:data-element-width min-width
          :data-layout        layout
          :style              style}))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;
  ; @return (map)
  [select-id select-props]
  (merge (element.helpers/element-default-attributes select-id select-props)
         (element.helpers/element-outdent-attributes select-id select-props)))
