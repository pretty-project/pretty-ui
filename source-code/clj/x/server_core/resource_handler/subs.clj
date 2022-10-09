
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-core.resource-handler.subs
    (:require [re-frame.api                      :as r :refer [r]]
              [x.server-core.config-handler.subs :as config-handler.subs]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn get-resource-handlers
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @return (maps in vector)
  ;  [{:path (string)
  ;    :root (string)}]
  [db _]
  (r config-handler.subs/get-server-config-item db :resources))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; WARNING! NON-PUBLIC! DO NOT USE!
(r/reg-sub :core/get-resource-handlers get-resource-handlers)
