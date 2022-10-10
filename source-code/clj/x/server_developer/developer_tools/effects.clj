
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.developer-tools.effects
    (:require [mid-fruits.pretty                         :as pretty]
              [re-frame.api                              :as r :refer [r]]
              [x.server-core.api                         :as core]
              [x.server-developer.developer-tools.routes :as developer-tools.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer/init!
  (fn [{:keys [db]} _]
      (if (r core/dev-mode? db)
          {:dispatch-n [[:router/add-route! :developer.developer-tools/route
                                            {:route-template "/developer-tools"
                                             :get (fn [request] (developer-tools.routes/download-developer-tools request))}]
                        [:router/add-route! :developer.developer-tools/extended-route
                                            {:route-template "/developer-tools/:tool-id"
                                             :get (fn [request] (developer-tools.routes/download-developer-tools request))}]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer/dump-db!
  (fn [{:keys [db]} _]
      (let [pretty-db (pretty/mixed->string db)]
           (println pretty-db))))
