
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.09
; Description:
; Version: v0.3.6
; Compatibility: x4.5.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.shield
    (:require [x.mid-ui.api      :as ui]
              [x.server-core.api :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (*) content
  ;
  ; @return (vector)
  [content]
  (let [title (a/subscribed [:core/get-app-config-item :app-title])]
       [:div#x-app-shield {:data-nosnippet "true"}
                          [:div#x-app-shield--header (ui/app-logo)
                                                     (ui/app-title title)]
                          [:div#x-app-shield--content content]]))
