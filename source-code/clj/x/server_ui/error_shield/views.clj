
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-ui.error-shield.views)



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @param (map) request
  [request]
  [:div#x-error-shield {:data-nosnippet "true"}
                       [:div#x-error-shield--body [:div#x-error-shield--content]
                                                  [:a#x-error-shield--refresh-button {:href    "#"
                                                                                      :onClick "location.reload ();"}
                                                                                     "Refresh"]]])
