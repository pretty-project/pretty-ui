

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns layouts.popup-b.views
    (:require [layouts.popup-b.helpers    :as helpers]
              [layouts.popup-b.prototypes :as prototypes]
              [x.app-components.api       :as components]
              [x.app-core.api             :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- layout-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ;  {:content (metamorphic-content)}
  [popup-id {:keys [content]}]
  [:div.popup-b--content [components/content popup-id content]])

(defn- popup-b
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ;  {:close-by-cover? (boolean)(opt)}
  [popup-id {:keys [close-by-cover?] :as layout-props}]
  [:div.popup-b (helpers/layout-attributes popup-id layout-props)
                [:div.popup-b--cover (if close-by-cover? {:on-click #(a/dispatch [:ui/close-popup! popup-id])})]
                [layout-structure popup-id layout-props]])

(defn layout
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) popup-id
  ; @param (map) layout-props
  ;  {:close-by-cover? (boolean)(opt)
  ;   :content (metamorphic-content)
  ;   :style (map)(opt)}
  [popup-id layout-props]
  (let [layout-props (prototypes/layout-props-prototype layout-props)]
       [popup-b popup-id layout-props]))
