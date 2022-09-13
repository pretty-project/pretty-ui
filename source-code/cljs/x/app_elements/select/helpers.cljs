
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.select.helpers
    (:require [x.app-core.api                 :as a]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-elements.select.config   :as select.config]
              [x.app-environment.api          :as environment]))



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
       (a/dispatch [:elements.select/active-button-did-mount select-id select-props])))

(defn active-button-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) select-id
  ; @param (map) select-props
  ;  {}
  ;
  ; @return (function)
  [select-id select-props]
  #(a/dispatch [:elements.select/active-button-will-unmount select-id select-props]))



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
        required-warning? @(a/subscribe [:elements.select/required-warning? select-id select-props])]
       (if disabled? {:disabled          true
                      :data-border-color (if required-warning? :warning :highlight)}
                     {:data-clickable    true
                      :on-click          #(a/dispatch on-click)
                      :on-mouse-up       #(environment/blur-element!)
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
  (let [selected-value  @(a/subscribe [:db/get-item value-path])
        option-selected? (= selected-value option)
        on-click         [:elements.select/select-option! select-id select-props option]]
       {:data-selected option-selected?
        :on-click     #(a/dispatch on-click)
        :on-mouse-up  #(environment/blur-element!)}))
