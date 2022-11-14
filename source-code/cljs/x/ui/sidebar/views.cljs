
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.ui.sidebar.views
    (:require [re-frame.api     :as r]
              [x.components.api :as x.components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [sidebar-content @(r/subscribe [:x.ui.sidebar/get-sidebar-content])]
          [:div#x-app-sidebar--content [x.components/content ::view sidebar-content]]))

(defn- sidebar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [sidebar-hidden? @(r/subscribe [:x.ui.sidebar/sidebar-hidden?])]
          [:<>]
          [:div#x-app-sidebar {:data-nosnippet true}
                              [sidebar-content]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [sidebar])
