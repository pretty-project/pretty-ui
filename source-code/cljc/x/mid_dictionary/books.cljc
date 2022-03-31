
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
              [x.mid-dictionary.books.website       :as books.website]
              [x.mid-dictionary.extension-books.actions      :as extension-books.actions]
              [x.mid-dictionary.extension-books.calendar     :as extension-books.calendar]
              [x.mid-dictionary.extension-books.charts       :as extension-books.charts]
              [x.mid-dictionary.extension-books.clients      :as extension-books.clients]
              [x.mid-dictionary.extension-books.devices      :as extension-books.devices]
              [x.mid-dictionary.extension-books.employees    :as extension-books.employees]
              [x.mid-dictionary.extension-books.inventories  :as extension-books.inventories]
              [x.mid-dictionary.extension-books.jobs         :as extension-books.jobs]
              [x.mid-dictionary.extension-books.machines     :as extension-books.machines]
              [x.mid-dictionary.extension-books.price-quotes :as extension-books.price-quotes]
              [x.mid-dictionary.extension-books.products     :as extension-books.products]
              [x.mid-dictionary.extension-books.services     :as extension-books.services]
              [x.mid-dictionary.extension-books.storage      :as extension-books.storage]
              [x.mid-dictionary.extension-books.vehicles     :as extension-books.vehicles]
              [x.mid-dictionary.extension-books.websites     :as extension-books.websites]))



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
                  books.website/BOOK
                  extension-books.actions/BOOK
                  extension-books.calendar/BOOK
                  extension-books.charts/BOOK
                  extension-books.clients/BOOK
                  extension-books.devices/BOOK
                  extension-books.employees/BOOK
                  extension-books.inventories/BOOK
                  extension-books.jobs/BOOK
                  extension-books.machines/BOOK
                  extension-books.price-quotes/BOOK
                  extension-books.products/BOOK
                  extension-books.services/BOOK
                  extension-books.storage/BOOK
                  extension-books.vehicles/BOOK
                  extension-books.websites/BOOK))
