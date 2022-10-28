
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.label.helpers
    (:require [x.app-elements.element.helpers :as element.helpers]
              [x.app-elements.label.state     :as label.state]
              [x.app-environment.api          :as x.environment]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn info-text-visible?
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ;
  ; @return (boolean)
  [label-id]
  (get @label.state/INFO-TEXT-VISIBILITY label-id))

(defn toggle-info-text-visiblity!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  [label-id]
  (swap! label.state/INFO-TEXT-VISIBILITY update label-id not))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn label-body-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [_ {:keys [selectable? style]}]
  {:data-selectable selectable?
   :style           style})

(defn label-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [label-id {:keys [color font-size font-weight horizontal-align horizontal-position
                    min-width vertical-position] :as label-props}]
  (merge (element.helpers/element-default-attributes label-id label-props)
         (element.helpers/element-indent-attributes  label-id label-props)
         (element.helpers/apply-color {} :color :data-color color)
         {:data-font-size           font-size
          :data-font-weight         font-weight
          :data-horizontal-align    horizontal-align
          :data-horizontal-position horizontal-position
          :data-min-width           min-width
          :data-vertical-position   vertical-position}))

(defn label-info-text-button-attributes
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) label-id
  ; @param (map) label-props
  ;  {}
  ;
  ; @return (map)
  ;  {}
  [label-id _]
  {:data-clickable   true
   :data-selectable  false
   :data-icon-family :material-icons-filled
   :on-click        #(toggle-info-text-visiblity! label-id)
   :on-mouse-up     #(x.environment/blur-element!)})
