
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript web application framework
; https://monotech-hq.github.io/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns engines.engine-handler.errors.subs
    (:require [engines.engine-handler.body.subs :as body.subs]
              [re-frame.api                     :refer [r]]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn print-missing-handler-key
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) engine-id
  ;
  ; @return (nil)
  [db [_ engine-id]]
  (let [transfer-id (r body.subs/get-body-prop db engine-id :transfer-id)]
       (println "Missing handler-key!\nengine-id:"engine-id"\ntransfer-id:"transfer-id)
       (println "The handler-key property is important to the engines to send mutation"
                "and resolver queries!"
                "\nThis property should transferred with the engine properties during the"
                "application's boot progress!"
                "\nDid you pass the transfer-id prop to the engine properly? (not required)"
                "\nDid you set the server-side initialization event for the engine?"
                "\nDidn' you mistype the engine-id?")))
