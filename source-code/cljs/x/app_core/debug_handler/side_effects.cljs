
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.side-effects
    (:require [mid-fruits.uri                   :as uri]
              [re-frame.api                     :as r]
              [window.api                       :as window]
              [x.app-core.debug-handler.helpers :as debug-handler.helpers]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- detect-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (r/dispatch [:db/set-item! [:core :debug-handler/meta-items :debug-mode]
                                  (debug-handler.helpers/query-string->debug-mode query-string)])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-fx :core/detect-debug-mode! detect-debug-mode!)
