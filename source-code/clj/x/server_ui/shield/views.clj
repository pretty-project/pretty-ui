
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.shield.views
    (:require [x.server-ui.graphics.views :as graphics.views]
              [x.server-core.api          :as a]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) content
  [content]
  (let [title @(a/subscribe [:core/get-app-config-item :app-title])]
       [:div#x-app-shield {:data-nosnippet "true"}
                          [:div#x-app-shield--header (graphics.views/app-logo)
                                                     (graphics.views/app-title title)]
                          [:div#x-app-shield--content content]]))
