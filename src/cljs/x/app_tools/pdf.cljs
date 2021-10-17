
; WARNING! NOT TESTED! DO NOT USE!

; https://github.com/gered/clj-htmltopdf/blob/master/src/clj_htmltopdf/core.clj



;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2020.12.30
; Description:
; Version: v0.1.8
; Compatibility:



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-tools.pdf
    (:require [mid-fruits.random :as random]
              [x.app-core.api    :as a :refer [r]]
              [x.app-db.api      :as db]))



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- get-filename
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (string)
  [db [_ request-id]]
  (get-in db (db/path ::primary request-id :filename)))

(defn- get-temporary-file-id
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (string)
  [db [_ request-id]]
  (get-in db (db/path ::primary request-id :temporary-file-id)))

(defn- get-generated-pdf-uri
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  ;
  ; @return (string)
  [db [_ request-id]]
  (let [temporary-file-id (r get-temporary-file-id db request-id)
        filename          (r get-filename db request-id)]
       (str "/pdf/" temporary-file-id "/" filename)))



;; -- Effect events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::generate-pdf!
  ; @param (vector) content
  ;   XXX#9345
  ; @param (string) filename
  (fn [_ [_ content filename]]
      (let [request-id (random/generate-keyword)]
           [:x.app-sync/send-request!
            request-id
            {:method :post
             :on-success [::->pdf-generated request-id]
             :params {:content  content
                      :filename filename}
             :target-path (db/path ::primary request-id)
             :uri "/pdf/generate-pdf"}])))



;; -- Status events -----------------------------------------------------------
;; ----------------------------------------------------------------------------

(a/reg-event-fx
  ::->pdf-generated
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) request-id
  (fn [{:keys [db]} [_ request-id]]
      [:x.app-environment.window-handler/open-new-browser-tab!
       (r get-generated-pdf-uri db request-id)]))
