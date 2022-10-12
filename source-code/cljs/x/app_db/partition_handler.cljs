
;; -- Header ------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; Author: bithandshake
; Created: 2021.04.28
; Description:
; Version: v0.4.0
; Compatibility: x4.1.5



;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.app-db.partition-handler
    (:require [re-frame.api               :as r]
              [x.mid-db.partition-handler :as partition-handler]))



;; -- Redirects ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

; x.mid-db.partition-handler
(def data-item-path                 partition-handler/data-item-path)
(def path                           partition-handler/path)
(def meta-item-path                 partition-handler/meta-item-path)
(def get-partition                  partition-handler/get-partition)
(def get-data-items                 partition-handler/get-data-items)
(def get-data-item                  partition-handler/get-data-item)
(def data-item-exists?              partition-handler/data-item-exists?)
(def get-data-item-count            partition-handler/get-data-item-count)
(def get-meta-items                 partition-handler/get-meta-items)
(def get-meta-item                  partition-handler/get-meta-item)
(def get-data-order                 partition-handler/get-data-order)
(def partition-ordered?             partition-handler/partition-ordered?)
(def partition-empty?               partition-handler/partition-empty?)
(def reg-partition!                 partition-handler/reg-partition!)



;; -- Subscriptions -----------------------------------------------------------
;; ----------------------------------------------------------------------------

; @usage
;  [:db/get-partition :my-partition/primary]
(r/reg-sub :db/get-partition get-partition)

; @usage
;  [:db/partition-empty? :my-partition/primary]
(r/reg-sub :db/partition-empty? partition-empty?)
