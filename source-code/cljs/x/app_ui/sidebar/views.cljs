
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-ui.sidebar.views
    (:require [re-frame.api         :as r]
              [x.app-components.api :as components]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- sidebar-content
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [sidebar-content @(r/subscribe [:ui.sidebar/get-sidebar-content])]
          [:div#x-app-sidebar--content [components/content ::view sidebar-content]]))

(defn- sidebar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (if-let [sidebar-hidden? @(r/subscribe [:ui.sidebar/sidebar-hidden?])]
          [:<>]
          [:div#x-app-sidebar {:data-nosnippet true}
                              [sidebar-content]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [sidebar])
