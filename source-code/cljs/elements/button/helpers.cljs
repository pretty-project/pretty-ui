
(ns elements.button.helpers
    (:require [dom.api                       :as dom]
              [elements.element.helpers      :as element.helpers]
              [elements.element.side-effects :as element.side-effects]
              [hiccup.api                    :as hiccup]
              [re-frame.api                  :as r]
              [reagent.api                   :as reagent]))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-did-mount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  [button-id {:keys [keypress] :as button-props}]
  ; A component-did-mount életciklus eltárolja a Re-Frame adatbázisban a button elem
  ; billentyűlenyomás-általi vezérléséhez szükséges tulajdonságokat, így azok az elem
  ; billentyűlenyomás-vezérlője számára elérhetők lesznek az adatbázisban.
  (if keypress (r/dispatch [:elements.button/button-did-mount button-id button-props])))

(defn button-did-update
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

(defn button-will-unmount
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:keypress (map)(opt)}
  [button-id {:keys [keypress] :as button-props}]
  ; A component-will-unmount életciklus törli a Re-Frame adatbázisból a button elem
  ; eltárolt tulajdonságait.
  (if keypress (r/dispatch [:elements.button/button-will-unmount button-id button-props])))

;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn button-icon-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:icon-color (keyword or string)
  ;  :icon-family (keyword)
  ;  :font-size (keyword)}
  ;
  ; @return (map)
  ; {:data-icon-family (keyword)
  ;  :data-icon-size (keyword)}
  [_ {:keys [icon-color icon-family icon-size]}]
  (-> {:data-icon-family icon-family
       :data-icon-size   icon-size}
      (element.helpers/apply-color :icon-color :data-icon-color icon-color)))

(defn button-style-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:border-color (keyword or string)(opt)
  ;  :color (keyword or string)
  ;  :fill-color (keyword or string)(opt)
  ;  :hover-color (keyword)(opt)
  ;  :style (map)(opt)}
  ;
  ; @return (map)
  ; {:style (map)}
  [_ {:keys [border-color color fill-color hover-color style]}]
  (-> {:style style}
      (element.helpers/apply-color :border-color :data-border-color border-color)
      (element.helpers/apply-color :color        :data-color        color)
      (element.helpers/apply-color :fill-color   :data-fill-color   fill-color)
      (element.helpers/apply-color :hover-color  :data-hover-color  hover-color)))

(defn button-font-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:font-size (keyword)
  ;  :font-weight (keyword)
  ;  :line-height (keyword)}
  ;
  ; @return (map)
  ; {:data-font-size (keyword)
  ;  :data-font-weight (keyword)
  ;  :data-line-height (keyword)}
  [_ {:keys [font-size font-weight line-height]}]
  {:data-font-size   font-size
   :data-font-weight font-weight
   :data-line-height line-height})

(defn button-layout-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:border-radius (keyword)(opt)
  ;  :horizontal-align (keyword)}
  ;
  ; @return (map)
  ; {:data-border-radius (keyword)
  ;  :data-horizontal-row-align (keyword)}
  [_ {:keys [border-radius horizontal-align]}]
  {:data-border-radius        border-radius
   :data-horizontal-row-align horizontal-align})

(defn button-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) button-id
  ; @param (map) button-props
  ; {:disabled? (boolean)(opt)
  ;  :on-click (metamorphic-event)(opt)
  ;  :on-mouse-over (metamorphic-event)(opt)}
  ;
  ; @return (map)
  ; {:data-clickable (boolean)
  ;  :data-selectable (boolean)
  ;  :disabled (boolean)
  ;  :id (string)
  ;  :on-click (function)
  ;  :on-mouse-over (function)
  ;  :on-mouse-up (function)}
  [button-id {:keys [disabled? on-click on-mouse-over] :as button-props}]
  ; XXX#4460
  ; By setting the :id attribute the body component becomes targetable for
  ; DOM actions. (setting focus/blur, etc.)
  (merge (element.helpers/element-indent-attributes button-id button-props)
         (element.helpers/element-badge-attributes  button-id button-props)
         (button-style-attributes                   button-id button-props)
         (button-font-attributes                    button-id button-props)
         (button-layout-attributes                  button-id button-props)
         {:data-selectable false}
         (if disabled? {:disabled       true}
                       {:id             (hiccup/value button-id "body")
                        :on-click       #(r/dispatch  on-click)
                        :on-mouse-over  #(r/dispatch  on-mouse-over)
                        :on-mouse-up    #(element.side-effects/blur-element! button-id)
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
         (element.helpers/element-outdent-attributes button-id button-props)))
