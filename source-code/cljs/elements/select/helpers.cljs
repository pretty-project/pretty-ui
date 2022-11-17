
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.select.helpers
    (:require [candy.api                     :refer [return]]
              [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [elements.select.config        :as select.config]
              [elements.text-field.helpers   :as text-field.helpers]
              [re-frame.api                  :as r]
              [string.api                    :as string]
              [x.components.api              :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn active-button-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (function)
  [select-id {:keys [initial-options initial-value] :as select-props}]
  ; A {:layout :select} beállítással megjelenített select elem megjeleníti az aktuálisan kiválasztott
  ; értékét, ezért az elem React-fába csatolásakor szükséges meghívni az [:elements.select/active-button-did-mount ...]
  ; eseményt, hogy esetlegesen a Re-Frame adatbázisba írja az {:initial-value ...} kezdeti értéket!
  #(if (or initial-options initial-value)
       (r/dispatch [:elements.select/active-button-did-mount select-id select-props])))

(defn active-button-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (function)
  [select-id select-props]
  #(r/dispatch [:elements.select/active-button-will-unmount select-id select-props]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn render-option?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:option-label-f (function)(opt)}
  ; @param (*) option
  ;
  ; @return (boolean)
  [select-id {:keys [option-label-f] :as select-props} option]
  ; XXX#0714 (source-code/cljs/elements/combo_box/helpers.cljs)
  (let [field-content (text-field.helpers/get-field-content :elements.select/option-field)
        option-label  (option-label-f option)]
       (and (string/not-pass-with? (x.components/content option-label) field-content {:case-sensitive? false})
            (string/starts-with?   (x.components/content option-label) field-content {:case-sensitive? false}))))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:border-radius (keyword)
  ;   :layout (keyword)
  ;   :min-width (keyword)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-layout (keyword)
  ;   :data-min-width (keyword)}
  [select-id {:keys [border-radius layout min-width] :as select-props}]
  (merge (element.helpers/element-default-attributes select-id select-props)
         (element.helpers/element-indent-attributes  select-id select-props)
         {:data-border-radius border-radius
          :data-layout        layout
          :data-min-width     min-width}))

(defn select-button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {:disabled? (boolean)(opt)}
  ;
  ; @return (map)
  ;  {:data-clickable (boolean)
  ;   :disabled (boolean)
  ;   :on-click (function)
  ;   :on-mouse-up (function)}
  [select-id {:keys [disabled?] :as select-props}]
  (let [on-click          [:elements.select/render-options! select-id select-props]
        required-warning? @(r/subscribe [:elements.select/required-warning? select-id select-props])]
       (if disabled? {:disabled          true
                      :data-border-color (if required-warning? :warning :highlight)}
                     {:data-clickable    true
                      :on-click          #(r/dispatch on-click)
                      :on-mouse-up       #(element.side-effects/blur-element! select-id)
                      :data-border-color (if required-warning? :warning :highlight)})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn select-option-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ; @param (*) option
  ;
  ; @return (map)
  ;  {}
  [select-id {:keys [value-path] :as select-props} option]
  (let [selected-value  @(r/subscribe [:x.db/get-item value-path])
        option-selected? (= selected-value option)
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:data-selected option-selected?
        :on-click     #(r/dispatch on-click)
        :on-mouse-up  #(element.side-effects/blur-element! select-id)}))
