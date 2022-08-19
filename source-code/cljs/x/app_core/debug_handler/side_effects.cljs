
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-core.debug-handler.side-effects
    (:require [app-fruits.window                :as window]
              [mid-fruits.uri                   :as uri]
              [x.app-core.debug-handler.helpers :as debug-handler.helpers]
              [x.app-core.event-handler         :as event-handler :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- detect-debug-mode!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [_]
  (let [uri          (window/get-uri)
        query-string (uri/uri->query-string uri)]
       (event-handler/dispatch [:db/set-item! [:core :debug-handler/meta-items :debug-mode]
                                              (debug-handler.helpers/query-string->debug-mode query-string)])))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(event-handler/reg-fx :core/detect-debug-mode! detect-debug-mode!)
