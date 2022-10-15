
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns developer-tools.core.effects
    (:require [developer-tools.core.routes :as core.routes]
              [mid-fruits.pretty           :as pretty]
              [re-frame.api                :as r :refer [r]]
              [x.server-core.api           :as core]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer-tools.core/init!
  (fn [{:keys [db]} _]
      {:dispatch-n [[:router/add-route! :developer-tools.core/route
                                        {:route-template "/avocado-juice"
                                         :get            (fn [request] (core.routes/download-developer-tools request))
                                         :restricted?    (not (r core/dev-mode? db))}]
                    [:router/add-route! :developer-tools.core/extended-route
                                        {:route-template "/avocado-juice/:tool-id"
                                         :get            (fn [request] (core.routes/download-developer-tools request))
                                         :restricted?    (not (r core/dev-mode? db))}]]}))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(r/reg-event-fx :developer-tools.core/dump-db!
  (fn [{:keys [db]} _]
      (let [pretty-db (pretty/mixed->string db)]
           (println pretty-db))))
