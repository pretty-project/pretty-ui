
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.database-screen.views
    (:require [mid-fruits.pretty :as pretty]
              [x.app-core.api    :as a]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn view
  ; @usage
  ;  [developer/database-screen]
  []
  (let [db @(a/subscribe [:db/get-db])]
       [:div#x-database-screen [:pre (pretty/mixed->string db)]]))
