
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-developer.developer-tools.effects
    (:require [mid-fruits.pretty                         :as pretty]
              [x.server-core.api                         :as a :refer [r]]
              [x.server-developer.developer-tools.routes :as developer-tools.routes]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx 
  :developer/init!
  (fn [{:keys [db]} _]
      (println (r a/dev-mode? db))
      (if (r a/dev-mode? db)
          {:dispatch-n [[:router/add-route! :developer.developer-tools/route
                                            {:route-template "/developer-tools"
                                             :get (fn [request] (developer-tools.routes/download-developer-tools request))}]
                        [:router/add-route! :developer.developer-tools/extended-route
                                            {:route-template "/developer-tools/:tool-id"
                                             :get (fn [request] (developer-tools.routes/download-developer-tools request))}]]})))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :developer/dump-db!
  (fn [{:keys [db]} _]
      (let [pretty-db (pretty/mixed->string db)]
           (println pretty-db))))
