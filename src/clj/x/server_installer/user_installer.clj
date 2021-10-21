
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.10.18
; Description:
; Version: v0.2.2
; Compatibility: x4.4.1



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.server-installer.user-installer
    (:require [mid-fruits.candy          :refer [param return]]
              [mongo-db.api              :as mongo-db]
              [x.app-details             :as details]
              [x.server-core.api         :as a :refer [r]]
              [x.server-installer.engine :as engine]))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  :x.server-installer/install-user!
  ; WARNING! NON-PUBLIC! DO NOT USE!
  (fn [{:keys [db]} _]
      (println details/app-name "installing module: user")
      {:db (r engine/->module-installed db :user)
       :dispatch [:x.server-user/add-user! {:email-address "demo@monotech.hu"
                                            :password      "mono"}]}))



"
- Létrehozza a mongo-db kollekciókat.
- Létrehozza a user-eket.
- Létrehozza a project-details.edn fájlt (végre nem kell a mono-template-ben is up to date tartani)
- Létrehozza a media mappákat.
- Létrehozza a root mappát a directories kollekcioban
- Létrehozza a sample fájlt a files koll.
"
