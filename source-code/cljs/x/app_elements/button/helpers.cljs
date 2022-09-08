
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.button.helpers
    (:require [x.app-core.api                 :as a]
              [x.app-elements.element.helpers :as element.helpers]
              [x.app-environment.api          :as environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  ;
  ; @return (function)
  [button-id {:keys [keypress] :as button-props}]
  ; A component-did-mount életciklus eltárolja a Re-Frame adatbázisban a button elem
  ; billentyűlenyomás-általi vezérléséhez szükséges tulajdonságokat, így azok az elem
  ; billentyűlenyomás-vezérlője számára elérhetők lesznek az adatbázisban.
  #(if keypress (a/dispatch [:elements.button/button-did-mount button-id button-props])))

(defn button-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  ;
  ; @return (function)
  [button-id {:keys [keypress] :as button-props}]
  ; A component-did-update életciklus aktualizálja a Re-Frame adatbázisban a button
  ; elem eltárolt tulajdonságait, így azok követik a button elem számára paraméterként
  ; átadott button-props térkép változásait.
  #(if keypress (a/dispatch [:elements.button/button-did-update button-id button-props])))

(defn button-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  ;
  ; @return (function)
  [button-id {:keys [keypress] :as button-props}]
  ; A component-will-unmount életciklus törli a Re-Frame adatbázisból a button elem
  ; eltárolt tulajdonságait.
  #(if keypress (a/dispatch [:elements.button/button-will-unmount button-id button-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:icon-family (keyword)
  ;   :icon-position (keyword)}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [icon-family icon-position]}]
  {:data-icon-family   icon-family
   :data-icon-position icon-position})

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [button-id {:keys [disabled? on-click]}]
  (if disabled? {:disabled       true}
                {:data-clickable true
                 :id              (a/dom-value button-id "body")
                 :on-click       #(a/dispatch on-click)
                 :on-mouse-up    #(environment/blur-element!)}))

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:background-color (keyword or string)(opt)
  ;   :border-color (keyword or string)(opt)
  ;   :border-radius (keyword)(opt)
  ;   :color (keyword or string)
  ;   :font-size (keyword)
  ;   :font-weight (keyword)
  ;   :horizontal-align (keyword)
  ;   :hover-color (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-font-size (keyword)
  ;   :data-font-weight (keyword)
  ;   :data-horizontal-align (keyword)
  ;   :data-selectable (boolean)}
  [button-id {:keys [background-color border-color border-radius color font-size
                     font-weight horizontal-align hover-color] :as button-props}]
  (merge (element.helpers/element-default-attributes button-id button-props)
         (element.helpers/element-indent-attributes  button-id button-props)
         (element.helpers/apply-color {} :background-color :data-background-color background-color)
         (element.helpers/apply-color {} :border-color     :data-border-color         border-color)
         (element.helpers/apply-color {} :color            :data-color                       color)
         (element.helpers/apply-color {} :hover-color      :data-hover-color           hover-color)
         (if border-radius {:data-border-radius border-radius})
         {:data-font-size        font-size
          :data-font-weight      font-weight
          :data-horizontal-align horizontal-align
          :data-selectable       false}))
