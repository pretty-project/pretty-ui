
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.core.debug-handler.side-effects
    (:require [js-window.api                :as js-window]
              [re-frame.api                 :as r]
              [uri.api                      :as uri]
              [x.core.debug-handler.helpers :as debug-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- detect-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [uri          (js-window/get-uri)
        query-string (uri/to-query-string uri)]
       (r/dispatch [:x.db/set-item! [:x.core :debug-handler/meta-items :debug-mode]
                                    (debug-handler.helpers/query-string->debug-mode query-string)])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :x.core/detect-debug-mode! detect-debug-mode!)
