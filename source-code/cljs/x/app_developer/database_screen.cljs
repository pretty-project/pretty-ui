
(ns x.app-developer.database-screen
    (:require [mid-fruits.pretty    :as pretty]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- database-screen
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [view-id db]
  [:div#x-database-screen
    (str view-id)
    [:pre (pretty/mixed->string db)]])

(defn view
  ; @return (component)
  []
  [components/subscriber ::view
                         {:component  #'database-screen
                          :subscriber [:db/get-db]}])
