
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-elements.color-selector.views
    (:require [layouts.popup-a.api                      :as popup-a]
              [mid-fruits.random                        :as random]
              [x.app-elements.button.views              :as button.views]
              [x.app-elements.color-selector.helpers    :as color-selector.helpers]
              [x.app-elements.color-selector.prototypes :as color-selector.prototypes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- color-selector-option
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ; @param (string) option
  [selector-id selector-props option]
  [:button.x-color-selector--option (color-selector.helpers/color-selector-option-attributes selector-id selector-props option)
                                    [:div.x-color-selector--option--color {:style {:background-color option}}]])

(defn- color-selector-option-list
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  ;  {:options (vector)}
  [selector-id {:keys [options] :as selector-props}]
  (letfn [(f [option-list option] (conj option-list [color-selector-option selector-id selector-props option]))]
         (reduce f [:<>] options)))

(defn- color-selector-body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:div.x-color-selector--body (color-selector.helpers/color-selector-body-attributes selector-id selector-props)
                               [color-selector-option-list                            selector-id selector-props]])

(defn- color-selector-options-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [popup-a/layout :elements.color-selector/options
                  {:body [color-selector-body selector-id selector-props]}])

(defn color-selector-options
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) selector-id
  ; @param (map) selector-props
  [selector-id selector-props]
  [:div.x-color-selector--options (color-selector.helpers/color-selector-options-attributes selector-id selector-props)
                                  [color-selector-options-structure                         selector-id selector-props]])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn element
  ; XXX#0714
  ; A color-selector elem alapkomponense a button elem.
  ; A color-selector elem további paraméterezését a button elem dokumentációjában találod.
  ;
  ; @param (keyword)(opt) selector-id
  ; @param (map) selector-props
  ;  {:class (keyword or keywords in vector)(opt)
  ;   :options (strings in vector)(opt)
  ;   :options-label (metamorphic-content)(opt)
  ;   :options-path (vector)(opt)
  ;   :style (map)(opt)
  ;   :value-path (vector)(opt)}
  ;
  ; @usage
  ;  [color-selector {...}]
  ;
  ; @usage
  ;  [color-selector :my-color-selector {...}]
  ([selector-props]
   [element (random/generate-keyword) selector-props])

  ([selector-id selector-props]
   (let [button-props (color-selector.prototypes/button-props-prototype selector-id selector-props)]
        [button.views/element selector-id button-props])))
