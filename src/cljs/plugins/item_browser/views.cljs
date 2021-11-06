
(ns plugins.item-browser.views
    (:require [mid-fruits.candy            :refer [param return]]
              [plugins.item-browser.engine :as engine]
              [x.app-db.api                :as db]
              [x.app-components.api        :as components]
              [x.app-core.api              :as a :refer [r]]
              [x.app-elements.api          :as elements]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

;; -- Action components -------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn home-button
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @usage
  ;  [item-browser/home-button "media" {:at-home? false}]
  ;
  ; @return (component)
  [extension-name {:keys [at-home?]}]
  [elements/button ::home-button
                   ;:on-click  [:media/go-home!]
                   {:on-click  [(keyword extension-name "go-home!")]
                    :disabled? at-home?
                    :preset    :home-icon-button}])

(defn up-button
  ; @param (string) extension-name
  ; @param (map) element-props
  ;  {:at-home? (boolean)}
  ;
  ; @usage
  ;  [item-browser/up-button "media" {:at-home? false}]
  ;
  ; @return (component)
  [extension-name {:keys [at-home?]}]
  [elements/button ::up-button
                   {:disabled? at-home?
                   ;:on-click  [:media/go-up!]
                    :on-click  [(keyword extension-name "go-up!")]
                    :preset    :up-icon-button}])
