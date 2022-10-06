
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
  (let [sidebar-content @(r/subscribe [:ui.sidebar/get-content])]
       [:div#x-app-sidebar--content [components/content ::view sidebar-content]]))

(defn- sidebar
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  (let [sidebar-hidden? @(r/subscribe [:ui.sidebar/hidden?])]
       [:div#x-app-sidebar {:data-hidden    sidebar-hidden?
                            :data-nosnippet true}
                           [sidebar-content]]))

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  []
  [sidebar])
