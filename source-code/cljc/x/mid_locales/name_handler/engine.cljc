
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-locales.name-handler.engine
    (:require [x.mid-locales.name-handler.config :as name-handler.config]))



;; -- Helpers -----------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn name->ordered-name
  ; @param (string) first-name
  ; @param (string) last-name
  ; @param (keyword) locale-id
  ;
  ; @return (string)
  [first-name last-name locale-id]
  (let [name-order (get name-handler.config/NAME-ORDERS locale-id)]
       (if (= name-order :reversed)
           (str last-name  " " first-name)
           (str first-name " " last-name))))
