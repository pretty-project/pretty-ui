
(ns elements.select.helpers
    (:require [candy.api                   :refer [return]]
              [elements.element.helpers    :as element.helpers]
              [elements.select.config      :as select.config]
              [elements.text-field.helpers :as text-field.helpers]
              [re-frame.api                :as r]
              [string.api                  :as string]
              [x.components.api            :as x.components]
              [x.environment.api           :as x.environment]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn active-button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {:initial-options (vector)(opt)
  ;  :initial-value (*)(opt)}
  [select-id {:keys [initial-options initial-value] :as select-props}]
  ; A {:layout :select} beállítással megjelenített select elem megjeleníti az aktuálisan kiválasztott
  ; értékét, ezért az elem React-fába csatolásakor szükséges meghívni az [:elements.select/active-button-did-mount ...]
  ; eseményt, hogy esetlegesen a Re-Frame adatbázisba írja az {:initial-value ...} kezdeti értéket!
  (if (or initial-options initial-value)
      (r/dispatch [:elements.select/active-button-did-mount select-id select-props])))

(defn active-button-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  [select-id select-props]
  (r/dispatch [:elements.select/active-button-will-unmount select-id select-props]))

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
  (let [field-content (text-field.helpers/get-field-content :elements.select/option-field)
        option-label  (-> option option-label-f x.components/content)]
       (and (string/not-pass-with? option-label field-content {:case-sensitive? false})
            (string/starts-with?   option-label field-content {:case-sensitive? false}))))

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
  ; {:data-border-radius (keyword)
  ;  :data-clickable (boolean)
  ;  :disabled (boolean)
  ;  :on-click (function)
  ;  :on-mouse-up (function)}
  [select-id {:keys [border-color border-radius disabled?] :as select-props}]
  (let [on-click [:elements.select/render-options! select-id select-props]]
       (merge {:data-border-radius border-radius
               :data-border-color  border-color}
              (if disabled? {:disabled       true}
                            {:data-clickable true
                             :on-click       #(r/dispatch on-click)
                             :on-mouse-up    #(x.environment/blur-element! select-id)}))))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ; {}
  ; @param (*) option
  ;
  ; @return (map)
  ; {}
  [select-id {:keys [option-value-f value-path] :as select-props} option]
  (let [selected-value  @(r/subscribe [:x.db/get-item value-path])
        option-value     (option-value-f option)
        option-selected? (= selected-value option-value)
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:data-clickable   true
        :data-font-size   :s
        :data-font-weight (if option-selected? :extra-bold :bold)
        :data-selected    option-selected?
        :on-click         #(r/dispatch on-click)
        :on-mouse-up      #(x.environment/blur-element! select-id)}))
