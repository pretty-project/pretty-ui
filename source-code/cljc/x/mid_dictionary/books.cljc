
;; -- Legal information -------------------------------------------------------
;; ----------------------------------------------------------------------------

; Monoset Clojure/ClojureScript Library
; https://monotech.hu/monoset
;
; Copyright Adam Szűcs and other contributors - All rights reserved



;; -- Namespace ---------------------------------------------------------------
;; ----------------------------------------------------------------------------

(ns x.mid-dictionary.books
    (:require [x.mid-dictionary.books.actions       :as books.actions]
              [x.mid-dictionary.books.application   :as books.application]
              [x.mid-dictionary.books.archive       :as books.archive]
              [x.mid-dictionary.books.changes       :as books.changes]
              [x.mid-dictionary.books.colors        :as books.colors]
              [x.mid-dictionary.books.company       :as books.company]
              [x.mid-dictionary.books.contacts      :as books.contacts]
              [x.mid-dictionary.books.cookies       :as books.cookies]
              [x.mid-dictionary.books.database      :as books.database]
              [x.mid-dictionary.books.developer     :as books.developer]
              [x.mid-dictionary.books.edit          :as books.edit]
              [x.mid-dictionary.books.error-pages   :as books.error-pages]
              [x.mid-dictionary.books.error-reports :as books.error-reports]
              [x.mid-dictionary.books.errors        :as books.errors]
              [x.mid-dictionary.books.failures      :as books.failures]
              [x.mid-dictionary.books.favorites     :as books.favorites]
              [x.mid-dictionary.books.filters       :as books.filters]
              [x.mid-dictionary.books.inputs        :as books.inputs]
              [x.mid-dictionary.books.item          :as books.item]
              [x.mid-dictionary.books.languages     :as books.languages]
              [x.mid-dictionary.books.law           :as books.law]
              [x.mid-dictionary.books.layout        :as books.layout]
              [x.mid-dictionary.books.media         :as books.media]
              [x.mid-dictionary.books.network       :as books.network]
              [x.mid-dictionary.books.notifications :as books.notifications]
              [x.mid-dictionary.books.order-by      :as books.order-by]
              [x.mid-dictionary.books.search        :as books.search]
              [x.mid-dictionary.books.selection     :as books.selection]
              [x.mid-dictionary.books.seo           :as books.seo]
              [x.mid-dictionary.books.social-media  :as books.social-media]
              [x.mid-dictionary.books.sync          :as books.sync]
              [x.mid-dictionary.books.themes        :as books.themes]
              [x.mid-dictionary.books.temporary     :as books.temporary]
              [x.mid-dictionary.books.transfer      :as books.transfer]
              [x.mid-dictionary.books.units         :as books.units]
              [x.mid-dictionary.books.user          :as books.user]
              [x.mid-dictionary.books.view          :as books.view]
              [x.mid-dictionary.books.website       :as books.website]))



;; ----------------------------------------------------------------------------
;; ----------------------------------------------------------------------------

; @constant (map)
(def BOOKS (merge books.actions/BOOK
                  books.application/BOOK
                  books.archive/BOOK
                  books.changes/BOOK
                  books.colors/BOOK
                  books.company/BOOK
                  books.contacts/BOOK
                  books.cookies/BOOK
                  books.database/BOOK
                  books.developer/BOOK
                  books.edit/BOOK
                  books.error-pages/BOOK
                  books.error-reports/BOOK
                  books.errors/BOOK
                  books.failures/BOOK
                  books.favorites/BOOK
                  books.filters/BOOK
                  books.inputs/BOOK
                  books.item/BOOK
                  books.languages/BOOK
                  books.law/BOOK
                  books.layout/BOOK
                  books.media/BOOK
                  books.network/BOOK
                  books.notifications/BOOK
                  books.order-by/BOOK
                  books.search/BOOK
                  books.selection/BOOK
                  books.seo/BOOK
                  books.social-media/BOOK
                  books.sync/BOOK
                  books.temporary/BOOK
                  books.themes/BOOK
                  books.transfer/BOOK
                  books.units/BOOK
                  books.user/BOOK
                  books.view/BOOK
                  books.website/BOOK))
