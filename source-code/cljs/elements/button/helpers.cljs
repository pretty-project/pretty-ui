
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns elements.button.helpers
    (:require [dom.api                  :as dom]
              [elements.element.helpers :as element.helpers]
              [hiccup.api               :as hiccup]
              [re-frame.api             :as r]
              [reagent.api              :as reagent]
              [x.app-environment.api    :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-did-mount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  [button-id {:keys [keypress] :as button-props}]
  ; A component-did-mount életciklus eltárolja a Re-Frame adatbázisban a button elem
  ; billentyűlenyomás-általi vezérléséhez szükséges tulajdonságokat, így azok az elem
  ; billentyűlenyomás-vezérlője számára elérhetők lesznek az adatbázisban.
  (if keypress (r/dispatch [:elements.button/button-did-mount button-id button-props])))

(defn button-did-update-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (?) %
  [button-id %]
  ; A component-did-update életciklus aktualizálja a Re-Frame adatbázisban a button
  ; elem eltárolt tulajdonságait, így azok követik a button elem számára paraméterként
  ; átadott button-props térkép változásait.
  (let [[_ {:keys [keypress] :as button-props}] (reagent/arguments %)]
       (if keypress (r/dispatch [:elements.button/button-did-update button-id button-props]))))

(defn button-will-unmount-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:keypress (map)(opt)}
  [button-id {:keys [keypress] :as button-props}]
  ; A component-will-unmount életciklus törli a Re-Frame adatbázisból a button elem
  ; eltárolt tulajdonságait.
  (if keypress (r/dispatch [:elements.button/button-will-unmount button-id button-props])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn on-click-f
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-click (metamorphic-event)(opt)
  ;   :stop-propagation? (boolean)(opt)}
  [_ {:keys [disabled? on-click stop-propagation?]}]
  (if disabled? (fn [%] (if stop-propagation? (dom/stop-propagation! %)))
                (fn [%] (if stop-propagation? (dom/stop-propagation! %))
                        (r/dispatch on-click))))



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

(defn button-color-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:background-color (keyword or string)(opt)
  ;   :border-color (keyword or string)(opt)
  ;   :color (keyword or string)
  ;   :hover-color (keyword)(opt)
  ;   :style (map)(opt)}
  ;
  ; @return (map)
  ;  {:style (map)}
  [_ {:keys [background-color border-color color hover-color style]}]
  (-> {:style style}
      (element.helpers/apply-color :background-color :data-background-color background-color)
      (element.helpers/apply-color :border-color     :data-border-color         border-color)
      (element.helpers/apply-color :color            :data-color                       color)
      (element.helpers/apply-color :hover-color      :data-hover-color           hover-color)))

(defn button-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:font-size (keyword)
  ;   :font-weight (keyword)}
  ;
  ; @return (map)
  ;  {:data-font-size (keyword)
  ;   :data-font-weight (keyword)}
  [_ {:keys [font-size font-weight]}]
  {:data-font-size   font-size
   :data-font-weight font-weight})

(defn button-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:border-radius (keyword)(opt)
  ;   :horizontal-align (keyword)(opt)}
  ;
  ; @return (map)
  ;  {:data-border-radius (keyword)
  ;   :data-horizontal-align (keyword)}
  [_ {:keys [border-radius horizontal-align]}]
  {:data-border-radius    border-radius
   :data-horizontal-align horizontal-align})

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;  {:disabled? (boolean)(opt)
  ;   :on-mouse-over (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ;  {:data-clickable (boolean)
  ;   :data-selectable (boolean)
  ;   :disabled (boolean)
  ;   :id (string)
  ;   :on-click (function)
  ;   :on-mouse-over (function)
  ;   :on-mouse-up (function)}
  [button-id {:keys [disabled? on-mouse-over] :as button-props}]
  (merge {:data-selectable false}
         (button-color-attributes  button-id button-props)
         (button-font-attributes   button-id button-props)
         (button-layout-attributes button-id button-props)
         (if disabled? {:disabled       true
                        :on-click       (on-click-f   button-id button-props)}
                       {:id             (hiccup/value button-id "body")
                        :on-click       (on-click-f   button-id button-props)
                        :on-mouse-over  #(r/dispatch on-mouse-over)
                        :on-mouse-up    #(x.environment/blur-element!)
                        :data-clickable true})))

(defn button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ;
  ; @return (map)
  [button-id button-props]
  (merge (element.helpers/element-default-attributes button-id button-props)
         (element.helpers/element-indent-attributes  button-id button-props)))
