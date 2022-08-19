

;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors
; Released under the xxx license



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns settings.view-selector.views
    (:require [settings.appearance-settings.views   :rename {body appearance-settings}]
              [settings.notification-settings.views :rename {body notification-settings}]
              [settings.personal-settings.views     :rename {body personal-settings}]
              [settings.privacy-settings.views      :rename {body privacy-settings}]
              [settings.view-selector.helpers       :as view-selector.helpers]
              [plugins.view-selector.api            :as view-selector]
              [x.app-core.api                       :as a]
              [x.app-elements.api                   :as elements]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn body-structure
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [current-view-id @(a/subscribe [:view-selector/get-current-view-id :settings.view-selector])]
       (case current-view-id :personal      [personal-settings     :settings.view-selector/view]
                             :privacy       [privacy-settings      :settings.view-selector/view]
                             :notifications [notification-settings :settings.view-selector/view]
                             :appearance    [appearance-settings   :settings.view-selector/view]
                                            [personal-settings     :settings.view-selector/view])))

(defn body
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [view-selector/body :settings.view-selector
                      {:content         #'body-structure
                       :default-view-id :personal}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn menu-bar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [elements/menu-bar ::menu-bar
                     {:menu-items (view-selector.helpers/menu-bar-items)
                      :horizontal-align :center}])

(defn header
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  [elements/horizontal-polarity ::header
                                {:middle-content [menu-bar]}])



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [surface-id])
  ; WARNING! DEPRECATED! DO NOT USE!
  ;[layouts/layout-a ::view
  ;                  {:body   #'body
  ;                   :header #'header])
