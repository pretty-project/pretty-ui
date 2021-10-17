
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.02.07
; Description:
; Version: v0.1.0
; Compatibility: x3.9.9



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-developer.database-screen
    (:require [mid-fruits.pretty    :as pretty]
              [x.app-components.api :as components]
              [x.app-core.api       :as a :refer [r]]))



;; -- Components --------------------------------------------------------------
;; ----------------------------------------------------------------------------

(defn- database-screen
  ; WARNING! NON-PUBLIC! DO NOT USE!
  ;
  ; @param (keyword) component-id
  ; @param (map) db
  ;
  ; @return (hiccup)
  [component-id db]
  [:div#x-database-screen
   (str component-id)
   [:pre (pretty/mixed->string db)]])

(defn view
  ; @return (component)
  []
  [components/subscriber {:component  #'database-screen
                          :subscriber [:x.app-db/get-db]}])
