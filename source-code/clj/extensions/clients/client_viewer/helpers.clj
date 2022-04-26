
;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns extensions.clients.client-viewer.helpers
    (:require [x.server-locales.api :as locales]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn client-item<-name-field
  ; WARNING! NON-PUBLIC! DO NOT USE!
  [{:keys [request]} {:client/keys [first-name last-name] :as client-item}]
  ; XXX#7601
  ; Ha az item-viewer plugin kliens-oldali kezelője {:auto-title? true} beállítással van használva,
  ; akkor a :client/name virtuális mezőt szükséges hozzáadni a dokumentumhoz!
  (let [name-order (locales/request->name-order request)]
       (case name-order :reversed (assoc client-item :client/name (str last-name  " " first-name))
                                  (assoc client-item :client/name (str first-name " " last-name)))))
